## _THIS IS NOT PRODUCTION READY_

# Tune Up

A [Mesos](https://mesos.apache.org) framework that records scalar resource offers to Graphite or [InfluxDB](https://www.influxdb.com).

## To Run

To run this project, clone the repository and execute the commands below. The examples provided were run on OS X with Mesos using `--registry=in_memory`.

    $ LD_LIBRARY=/usr/local/lib MESOS_NATIVE_JAVA_LIBRARY=/usr/local/lib/libmesos.dylib sbt "run --mesos_master 127.0.0.1:5050 --mesos_user $(whoami)"
    [info] Loading project definition from tuneup/project
    [info] Set current project to tuneup (in build file:tuneup/)
    [info] Compiling 2 Scala sources to tuneup/target/scala-2.11/classes...
    [info] Running tuneup.Main --mesos_master 127.0.0.1:5050 --mesos_user <USER>
    [2015-03-24 20:25:02,494] INFO Starting TuneUp service... (tuneup.services.TuneupService:18)
    I0324 20:25:02.553751 383434752 sched.cpp:137] Version: 0.21.1
    I0324 20:25:02.556278 367546368 sched.cpp:234] New master detected at master@127.0.0.1:5050
    I0324 20:25:02.556500 367546368 sched.cpp:242] No credentials provided. Attempting to register without authentication
    [2015-03-24 20:25:02,556] INFO MesosDriver started successfully. (tuneup.mesos.MesosDriverFactory:33)
    I0324 20:25:02.557190 367546368 sched.cpp:408] Framework registered with 20150322-132755-16777343-5050-1432-0018

### Reporting Configuration Options

| Argument | InfluxDB Term | Graphite Term |
| -------- | ------------- | ------------- |
| report_db | InfluxDB database name | Series prefix (`<report_db>.slave.<UUID>.resource.cpu`) |
| report_host | Hostname or IP address of server. | Hostname or IP address of server. |
| report_port | Port used by sever (usually: `8086`). | Port used by server (usually: `2003`). |
| report_user | Username with access to `report_db`. | Not used. |
| report_pass | Password for `report_user`. | Not used. |
| report_pool_size | Number of concurrent connections to maintain with server. | Maximum number of concurrent requests to server. |

### Mesos Configuration Options

| Argument | Description |
| -------- | ----------- |
| mesos_master | The URL for the Mesos master. For a single master, usually just `hostname:port`. For ZooKeeper aware clusters, a list of `hostname:port`, separated by commas, with a `zk://` prefix. Example: `zk://10.0.0.5:2081,10.0.0.6:2081`|
| mesos_user | The Unix/local user to use when launching tasks (not used). |
| mesos_role | The resource role to use when registering the framework. |
| mesos_failover_timeout | The number of seconds to elapse before a disconnected framework is considered terminated. |
| mesos_framework_name | The name to use when registering the framework. This will be the name that shows up under "Active Frameworks" in the Mesos web UI. |

## Todo

- Add ZooKeeper
