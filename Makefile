runDev:
	./gradlew clean run --continuous --no-daemon --stacktrace

genProto:
    ./gradlew generateProto

seedSudoAdmin:
	./gradlew runSeeder
