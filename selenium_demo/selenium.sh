#set -euo pipefail
#IFS=$'\n\t'

mkdir -p bin

cp=".:./lib/*"
bin="./bin"

PATH="lib/:$PATH"

#sudo systemctl start nginx && 
    # when launching nginx container give it --name "selenium_site"
    ip=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' selenium_site)
    javac -cp "$cp:$bin" -d "$bin" src/SeleniumHelloWorld.java &&
    java -DcontainerURL="http://${ip}/index.html" -cp "$cp:$bin" org.testng.TestNG testng.xml;
#sudo systemctl stop nginx
