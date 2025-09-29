//package org.charitable.app.infrastructure.command;
//import io.micronaut.configuration.picocli.PicocliRunner;
//import jakarta.inject.Inject;
//import org.charitable.app.infrastructure.seeder.SudoAdminSeeder;
//import picocli.CommandLine.Option;
//import picocli.CommandLine.Command;
//
//@Command(name = "seed", description = "Seed initial data into the database")
//public class SeedCommand implements Runnable {
//
//    @Inject
//    private SudoAdminSeeder sudoAdminSeeder;
//
//    @Option(names = {"-a", "--admin"}, description = "Seed admin user")
//    boolean seedAdmin;
//
////    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Seed using ./gradlew seedSudoAdmin")
////    boolean helpRequested;
//
//    public static void main(String[] args) {
//        PicocliRunner.run(SeedCommand.class, args);
//    }
//
//    @Override
//    public void run() {
//        if (seedAdmin) {
//            System.out.println("Seeding admin user...");
//            sudoAdminSeeder.seedAdminUsers();
//        } else {
//            System.out.println("No seeding option selected. Use --help for options.");
//        }
//    }
//}
