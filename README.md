## _THIS IS NOT PRODUCTION READY_

# Tune Up

A [Mesos](https://mesos.apache.org) framework that records resource offers to time series database. Currently, 
only [InfluxDB](https://www.influxdb.com) is supported.

## Overview

Tune Up listens to resource offers and logs scalar values to a time series backend. [Dispatch](http://dispatch.databinder.net/Dispatch.html)
is used to interact with InfluxDB's REST API.

Data will be saved under the configured database with series names matching: `slave.<mesos assigned UUID>.resource.<cpu|mem|disk>`.

## To Run

There are no packaged releases at this time. To run this project, clone the repository and execute the commands below. 
The examples provided were run on OS X with Mesos using `--registry=in_memory`.

    $ sbt "run --help"
    [info] Running tuneup.Main --help
          --mesos_failover_timeout  <arg>   The failover timeout in milliseconds for
                                            Mesos (default = 3000)
          --mesos_framework_name  <arg>     The Mesos Framework name
                                            (default = tuneup-framework)
          --mesos_master  <arg>             The URL of the Mesos master.
                                            (default = local)
          --mesos_role  <arg>               The Mesos role to run tasks under
                                            (default = *)
          --mesos_user  <arg>               The Mesos user to run the processes under
                                            (default = root)
          --report_db  <arg>                The database to use when storing series
                                            data. (default = mesos_resources)
          --report_host  <arg>              The hostname that is running InfluxDB or
                                            Graphite. (default = 127.0.0.1)
          --report_pass  <arg>              The password to use when connecting to
                                            reporting backend. (default = root)
          --report_pool_size  <arg>         Number of reporting actors to maintain. A
                                            higher number should result in more
                                            concurrency. (default = 32)
          --report_port  <arg>              The port where InfluxDB or Graphite is
                                            listening. (default = 8086)
          --report_user  <arg>              The user that has access to the reporting
                                            database. (default = root)
          --help                            Show help message

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

## Configuration Notes

- `report_db` is "database name" in InfluxDB terms.

## Todo

- Add ZooKeeper
- Add Graphite support
- Add reconnection mechanics
