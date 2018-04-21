#!/bin/sh

backupVideos () {
    mv Reports/videos Reports/videos$1
    echo "Vidoes moved to Reports/videos$1"
}

stopEverything () {
    docker-compose stop
}

removeEverything () {
    echo "Deleting vidoes"
    rm -rf Reports/videos
    echo "Deleting allure report"
    rm -rf target/allure-results
    rm -rf target/site
}

BROWSER_STACK_ENABLED=false

#install homebrew if needed
command -v brew >/dev/null 2>&1 || {
    /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
}

brew tap caskroom/cask
brew update

brew install allure
brew cask install docker
brew cask install docker-toolbox

mkdir -p Reports/videos
docker pull elgalu/selenium
docker-compose build
docker-compose up -d zalenium
open http://localhost:4444/grid/admin/live?refresh=10
docker-compose run --rm d3tests
open http://localhost:5555
allure serve target/allure-results

DATE=$(date +"%Y%m%d%H%M")
echo "Do you want to stop and cleanup everything (you won't be able to access the allure report)"
select yn in "Yes" "Yes but backup the videos" ; do
    case $yn in
        "Yes" ) stopEverything $ALLURE_PID; removeEverything; break;;
        "Yes but backup the videos" ) stopEverything $ALLURE_PID; backupVideos $DATE; removeEverything; break;;
    esac
done

