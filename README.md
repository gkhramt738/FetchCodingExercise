This repository contains the code for the Fetch coding exercise for Stage 1: Take-Home Assignment.
The submitted code includes comprehensive comments in addition to citations for instances when code was directly incorporated from Stack Overflow, which was critical for coding and troubleshooting certain aspects of this app.

**Instructions to run this app:**
  1. Download this project as a .ZIP folder from https://github.com/gkhramt738/FetchCodingExercise by accessing the drop-down menu **Code --> Download .ZIP**.
  2. Once downloaded, unzip the project folder to a location of your choice.
  3. Upon launching the Android Studio application on your laptop or PC, click  **Open** to select a project to open.
  4. Navigate to the directory where the unzipped application is located, then click into the **parent folder FetchCodingExercise-master**
  5. Select the **child folder FetchCodingExercise-master** and click **OK** to open the Android app.
  6. Select a physical device to run the app, or use a virtual one by following the steps within Android Studio.
  7. Once your device has been configured, click the green forward arrow displayed in the top toolbar to launch the mobile application.

**Useful information for running this app successfully:**
  1. An active internet connection is required for this app to function properly and display the desired result.
  2. Configuration caches are designed to significantly improve build performance by "caching the result of the configuration phase and reusing this for subsequent builds".  If this cache configuration fails during project build and prevents you from launching the app follow the steps below.
    a. Open the **gradle.properties** file.
    b. At the very bottom of the file, at line 21, update "org.gradle.configuration-cache=true" to "org.gradle.configuration-cache=**false**", or delete this line.
    c. Clean the project by accessing **Build --> Clean Project**, then Rebuild the project by accessing **Build --> Rebuild**.
      * Sources: https://docs.gradle.org/current/userguide/configuration_cache.html, https://stackoverflow.com/questions/71793217/gradle-configuration-cache-state-could-not-be-cached
