{%-
    set driverDict = {
        'MariaDB': ['MariaDbEngineVersion', 'mariaDb', 'MariaDbEngineVersion.VER_10_5_13', 'AWSSecretsManagerMariaDBDriver', 'mariadb'],
        'MySQL': ['MysqlEngineVersion', 'mysql', 'MysqlEngineVersion.VER_8_0_26', 'AWSSecretsManagerMySQLDriver', 'mysql'],
        'PostgreSQL': ['PostgresEngineVersion', 'postgres', 'PostgresEngineVersion.VER_14_1', 'AWSSecretsManagerPostgreSQLDriver', 'postgresql']
    }
-%}
package {{project_group_id}}

import org.cdk8s.plus22.EnvValue
import software.amazon.awscdk.CfnOutput
import software.amazon.awscdk.Duration
import software.amazon.awscdk.Fn
import software.amazon.awscdk.NestedStack
import software.amazon.awscdk.NestedStackProps
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.InstanceType
import software.amazon.awscdk.services.ec2.Peer
import software.amazon.awscdk.services.ec2.Port
import software.amazon.awscdk.services.ec2.SecurityGroup
import software.amazon.awscdk.services.ec2.SubnetSelection
import software.amazon.awscdk.services.ec2.SubnetType
import software.amazon.awscdk.services.ec2.Vpc
import software.amazon.awscdk.services.ec2.VpcLookupOptions
import software.amazon.awscdk.services.rds.Credentials
import software.amazon.awscdk.services.rds.DatabaseInstance
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine
import software.amazon.awscdk.services.rds.{{driverDict[inputs.dbms][0]}}
import software.amazon.awscdk.services.secretsmanager.ISecret
import software.constructs.Construct

class RdsNestedStack(scope: Construct, id: String, stage: Stage) : NestedStack(scope, id, NestedStackProps.builder().build()) {
    val secret: ISecret

    init {
        val vpcId = stage.cloud.vpcId ?: throw IllegalStateException("The attribute cloud.vpcId is not present in the stage")
        val securityGroupOutputKey = stage.outputs?.get("securityGroupId")
            ?: throw IllegalStateException("The attribute outputs.securityGroupId is not present in the stage")
        val stackName = "$APP_NAME-stack"

        val vpc = Vpc.fromLookup(this, "$stackName-vpc-lookup", VpcLookupOptions.builder().vpcId(vpcId).build())

        val databaseName = "{{project_name|to_camel}}DB"

        val securityGroup = SecurityGroup.Builder
            .create(this, "$stackName-security-group")
            .vpc(vpc)
            .securityGroupName("$stackName-rds-security-group")
            .build()
        securityGroup.addIngressRule(Peer.securityGroupId(Fn.importValue(securityGroupOutputKey)), Port.allTraffic())

        val rdsDB = DatabaseInstance.Builder.create(this, "$stackName-rds")
            .engine(DatabaseInstanceEngine.{{driverDict[inputs.dbms][1]}} { {{driverDict[inputs.dbms][2]}} })
            .vpc(vpc)
            .monitoringInterval(Duration.seconds(60))
            .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PRIVATE_ISOLATED).build())
            .securityGroups(listOf(securityGroup))
            .credentials(Credentials.fromGeneratedSecret("{{project_name|to_camel}}User"))
            .databaseName(databaseName)
            .instanceIdentifier("$stackName-rds-instance")
            .instanceType(InstanceType.of(InstanceClass.BURSTABLE3, InstanceSize.MICRO))
            .build()

        this.secret = rdsDB.secret ?: throw IllegalStateException("Failed to create the Secret Manager")
        Manifests.env["SPRING_DATASOURCE_URL"] = EnvValue.fromValue("jdbc-secretsmanager:{{driverDict[inputs.dbms][4]}}://${rdsDB.dbInstanceEndpointAddress}/$databaseName")
        Manifests.env["SPRING_DATASOURCE_USERNAME"] = EnvValue.fromValue(this.secret.secretName)
        Manifests.env["SPRING_DATASOURCE_DRIVER_CLASS_NAME"] = EnvValue.fromValue("com.amazonaws.secretsmanager.sql.{{driverDict[inputs.dbms][3]}}")

        CfnOutput.Builder.create(this, "$stackName-arn-rds").value(rdsDB.instanceArn).exportName("$stackName-arn-rds").build()
    }
}
