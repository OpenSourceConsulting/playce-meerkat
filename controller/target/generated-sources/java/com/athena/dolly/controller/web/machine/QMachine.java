package com.athena.dolly.controller.web.machine;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QMachine is a Querydsl query type for Machine
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMachine extends EntityPathBase<Machine> {

    private static final long serialVersionUID = 1893856498L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMachine machine = new QMachine("machine");

    public final NumberPath<Float> cpuClockSpeed = createNumber("cpuClockSpeed", Float.class);

    public final StringPath cpuClockUnit = createString("cpuClockUnit");

    public final NumberPath<Integer> cpuCore = createNumber("cpuCore", Integer.class);

    public final com.athena.dolly.controller.web.datagridserver.QDatagridServer datagridServer;

    public final StringPath description = createString("description");

    public final CollectionPath<Disk, QDisk> disks = this.<Disk, QDisk>createCollection("disks", Disk.class, QDisk.class, PathInits.DIRECT2);

    public final NumberPath<Float> diskSize = createNumber("diskSize", Float.class);

    public final StringPath diskSizeUnit = createString("diskSizeUnit");

    public final CollectionPath<com.athena.dolly.controller.web.env.EnvironmentVariable, com.athena.dolly.controller.web.env.QEnvironmentVariable> environmentVariables = this.<com.athena.dolly.controller.web.env.EnvironmentVariable, com.athena.dolly.controller.web.env.QEnvironmentVariable>createCollection("environmentVariables", com.athena.dolly.controller.web.env.EnvironmentVariable.class, com.athena.dolly.controller.web.env.QEnvironmentVariable.class, PathInits.DIRECT2);

    public final StringPath hostName = createString("hostName");

    public final NumberPath<Integer> Id = createNumber("Id", Integer.class);

    public final BooleanPath isVm = createBoolean("isVm");

    public final StringPath jvmVersion = createString("jvmVersion");

    public final DateTimePath<java.util.Date> lastShutdown = createDateTime("lastShutdown", java.util.Date.class);

    public final CollectionPath<Memory, QMemory> memories = this.<Memory, QMemory>createCollection("memories", Memory.class, QMemory.class, PathInits.DIRECT2);

    public final NumberPath<Float> memorySize = createNumber("memorySize", Float.class);

    public final StringPath memorySizeUnit = createString("memorySizeUnit");

    public final StringPath name = createString("name");

    public final CollectionPath<NetworkInterface, QNetworkInterface> networkInterfaces = this.<NetworkInterface, QNetworkInterface>createCollection("networkInterfaces", NetworkInterface.class, QNetworkInterface.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> originDate = createDateTime("originDate", java.util.Date.class);

    public final StringPath osArch = createString("osArch");

    public final StringPath osName = createString("osName");

    public final StringPath osVersion = createString("osVersion");

    public final StringPath sshIPAddr = createString("sshIPAddr");

    public final StringPath sshPassword = createString("sshPassword");

    public final NumberPath<Integer> sshPort = createNumber("sshPort", Integer.class);

    public final StringPath sshUsername = createString("sshUsername");

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public final CollectionPath<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance> tomcatInstances = this.<com.athena.dolly.controller.web.tomcat.instance.TomcatInstance, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance>createCollection("tomcatInstances", com.athena.dolly.controller.web.tomcat.instance.TomcatInstance.class, com.athena.dolly.controller.web.tomcat.instance.QTomcatInstance.class, PathInits.DIRECT2);

    public QMachine(String variable) {
        this(Machine.class, forVariable(variable), INITS);
    }

    public QMachine(Path<? extends Machine> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMachine(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMachine(PathMetadata<?> metadata, PathInits inits) {
        this(Machine.class, metadata, inits);
    }

    public QMachine(Class<? extends Machine> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.datagridServer = inits.isInitialized("datagridServer") ? new com.athena.dolly.controller.web.datagridserver.QDatagridServer(forProperty("datagridServer"), inits.get("datagridServer")) : null;
    }

}

