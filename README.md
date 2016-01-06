# Magic Mirror
An application written with Spring Boot (Java) to create a multifunctional and easy extendable software gathering those information, you want to see if you look on your mirror.

## Prerequisites
You need a system with at least 1GB RAM, Java and MySQL.

I'm using a Raspberry Pi 2 to run the whole system. The application startup takes up to 10 minutes, although the server is designed to be never restarted. Once the application started up the system load is negligible.

Database settings are saved in ```src/main/java/resources/application.properties``` and can be adjusted.

Copy ```scripts/raspi-monitor``` to ```/usr/sbin/``` and make the file executable. This script is used to shutdown HDMI on the Pi which may sends your monitor to sleep.

## Software design & Features
Since I used Spring Boot the application is a server application naturally. To manage and display the aggregated and desired data, a HTML5 frontend and admin panel are provided. You can access those via

* ```host:port/``` for the frontend
* ```host:port/admin``` for the admin panel

At the first startup a super user account is created for the admin panel (```admin:password```). Its purpose is to create new user accounts.

Once you created your personal account, you can setup the weather information and your wanted data providers. Currently following providers are implemented:

* ```ical``` (showing todays events)
* ```imap``` (showing the amount of unread emails for an imap account)
* ```xkcd``` (planned)
* ```ruthe.de``` (planned)
* ```twitter``` (planned)

To set the update intervals of your data providers switch to the "Cron" tab in the backend and add a new cron action.

