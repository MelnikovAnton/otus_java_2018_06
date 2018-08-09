package ru.otus.gc;


import javax.management.*;
import java.lang.management.ManagementFactory;


public class Main {

    public final static long start = System.currentTimeMillis();

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {

        GcHelper.registerCollector();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Leaker");
        Leaker leaker = new Leaker();
       mbs.registerMBean(leaker, name);

       leaker.leak();

    }


}

