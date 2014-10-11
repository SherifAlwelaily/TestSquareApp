##Introduction

##Software requirements


##Project setup

### m2e-android plugin

This project is built using the [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html) to handle its external dependencies.

When using Eclipse ADT, it assumes that the following components are installed :

- Eclipse Market Client
- [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html)
- [maven-android-sdk-deployer](https://github.com/mosabua/)

### Eclipse MarketPlace

If you don't have the Eclipse Market Client installed, you can install it by clicking on 

```Help → Install new Software → Switch to the Juno Repository → General Purpose Tools → Marketplace Client```

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/1_available_soft.PNG)

Once you have the Eclipse Market Client installed, you can proceed to install the m2e-android plugin

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/2_marketplace.PNG)

```Help -> Eclipse Marketplace... and search for "android m2e".```

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/3_android_m2e.PNG)

More instructions can be found on the [m2e-android plugin](http://rgladwell.github.io/m2e-android/index.html) site.

### maven-android-sdk-deployer

Clone the [maven-android-sdk-deployer](https://github.com/mosabua/maven-android-sdk-deployer) and execute mvn install. This will install the required projects into your local maven repository.

### Environment setup

Make sure you have your ANDROID_HOME variable pointing to your SDK

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/4_env_variable.PNG)

Make sure you import Google Play Services into your Eclipse to avoid the dependency error below. 
M2E expects all library project dependencies to be present in the workspace as Maven projects.

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/5_playservices_import.PNG)
![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/6_dependency_warning.PNG)

### Library project setup

####Google Play Services

This project needs to be in your project workspace. The actual files should remain in the sdk folder.

    C:\SOFT\adt-bundle-windows-x86_64-20130729\sdk\extras\google\google_play_services\libproject\google-play-services_lib

You need to add the following pom.xml to this project

https://gist.github.com/ddewaele/6277487

This ensures that it is a mavenized project in your workspace. (simply drop the pom.xml in your project and do a `Convert to Maven` on the project.

####Compatibility-v7-appcompat

This project needs to be in your project workspace. The actual files should remain in the sdk folder.

    C:\SOFT\adt-bundle-windows-x86_64-20130729\sdk\extras\android\support\v7\appcompat

You need to add the following pom.xml to this project

https://gist.github.com/ddewaele/6277476

This ensures that it is a mavenized project in your workspace. (simply drop the pom.xml in your project and do a `Convert to Maven` on the project.


### Android Maps V2 API Key

This project uses the Google Maps V2 API for Android and requires you to retrieve an API key that you need to put in your manifest.
This API key is bound to the keystore that will be used to build/package your application.

there are a couple of steps you need to follow

- Register a project in the API console

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/7_register_key.PNG)

- Enable the Maps V2 API

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/8_enable_api.PNG)

- Create an Android Key

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/9_android_key.PNG)

- Specify your keystore SHA1 fingerprint.

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/10_android_keystore.PNG)

- Specify your package name

![](https://dl.dropboxusercontent.com/u/13246619/Blog%20Articles/AndroidMavenSetup/11_android_manifest.PNG)

- Add the API key to your Manifest.

Use the following element (child element of the application element)

	<meta-data android:name="com.google.android.maps.v2.API_KEY" android:value="INSERT_YOUR_API_KEY"/>
        
