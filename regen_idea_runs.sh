# helper script to regenerate outdated run configs left over after switching branches
rm -rf .idea/runConfigurations
./gradlew ideaSyncTask
