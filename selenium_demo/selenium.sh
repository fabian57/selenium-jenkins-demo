#set -euo pipefail
#IFS=$'\n\t'

cp=".:./lib/*"
bin="./bin"

mkdir -p bin

#sudo systemctl start nginx && 
#    cp -r ./www/* /usr/share/nginx/html &&
    javac -cp "$cp:$bin" -d "$bin" src/SeleniumHelloWorld.java &&
    java -cp "$cp:$bin" org.testng.TestNG testng.xml;
#sudo systemctl stop nginx
