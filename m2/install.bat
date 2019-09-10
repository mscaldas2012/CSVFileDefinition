cmd /C mvn install:install-file -Dfile=%PROJECTROOT0%/m2/pom.xml -DpomFile=%PROJECTROOT0%/m2/pom.xml
cmd /C mvn install:install-file -Dfile=%PROJECTROOT0%/m2/xcodebuild/pom.xml -DpomFile=%PROJECTROOT0%/m2/xcodebuild/pom.xml
cmd /C mvn install:install-file -Dfile=%PROJECTROOT0%/m2/sca-maven-plugin/sca-maven-plugin-18.20.jar -DpomFile=%PROJECTROOT0%/m2/sca-maven-plugin/pom.xml
