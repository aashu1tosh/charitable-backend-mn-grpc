runDev:
	./gradlew clean run --continuous --no-daemon --stacktrace

seedSudoAdmin:
	./gradlew seedSudoAdmin --no-daemon --stacktrace
