# ![Tandoor Logo](./src/main/resources/static/favicon-32x32.png "Tandoor Logo") Tandoor Dashboard

Simple way to display today's meal plan for example on a kitchen tablet and start cooking.

I started this project after I tried to integrate [Tandoor](https://tandoor.dev/) into
my [Home Assistant](https://www.home-assistant.io/) kitchen dashboard.
Tandoor does not like to be integrated as an iFrame. Alternatively I tried to create a couple of rest sensors with Home
Assistants [RESTFul integration](https://www.home-assistant.io/integrations/rest/) which turned out to be cumbersome.

However, I came up with this little application.

## Installation

Preferred way to install and run the application is docker.
You are able to run the application without docker if that's what you want to do.

### Without Docker

Make sure you install java 17 runtime environment depending on your operating system.

Windows: ``winget install -e --id Oracle.JDK.17``  
Debian: ``sudo apt install openjdk-17-jdk``  
Arch: ``pacman -S jdk17-openjdk``  
Mac: ``brew install openjdk@17 ``

Download the application from releases and run with ``java -jar tandoor-bashboard-0.0.1.jar``

### Docker

``docker run -p  8080:8080 -e tandoor.token="" -e tandoor.url="" ghcr.io/schmitzcatz/tandoor-dashboard:0.0.1 ``

```yaml
services:
  dashboard:
    image: ghcr.io/schmitzcatz/tandoor-dashboard:0.0.1
    ports:
      8080:8080
    environment:
      tandoor.token:
      tandoor.url:
```

## Usage

Not much to it, after installation and running you can view the dashboard with your web browser and embed it into
Home Assistant or any other website using [iFrames](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/iframe).

## Configuration

Easies way to configure the dashboard is to use environment variables,
if for some case you are bound to use a configuration file this is possible as well. Create a ``applicaion.properties``
file alongside the application jar or mount this into your docker container.

| Property       | Description                             | Default |
|----------------|-----------------------------------------|---------|
| tandoor.token  | Tandoor Authentication Token            |         |
| tandoor.url    | Tandoor Instance Url                    |         |
| page.refresh   | Seconds for auto reloading  (optional)  | 10      |
| page.colormode | Page color mode (light/dark) (optional) | dark    |

## Contribution

Feel free to star, fork, discuss and contribute to the project. If you are missing something,
or you find "anomalies" aka bugs, please let me know by opening an issue on GitHub.

## License

[GNU GPLv3](https://spdx.org/licenses/GPL-3.0-or-later.html)



