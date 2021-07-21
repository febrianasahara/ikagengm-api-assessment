## Ikageng Masie
## Interview Assessment (Code Evaluation)

[![N|Solid](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/playstore_icon.png?alt=media&token=58ffee3d-5a6d-4cd9-beb9-a8c856bf128b)](https://nodesource.com/products/nsolid)

This project is meant to demonstrate my (Ikageng Masie) development compitencies✨Let me show you the Magic!✨


## App Features

- Secure (no hardcoded sensitive data)
- Customised UI
- Remote Crash/Error Handling -> [FirebaseCrashlytics](https://firebase.google.com/products/crashlytics?gclid=Cj0KCQjw6NmHBhD2ARIsAI3hrM2zXBo0Ub4JqqR-Jhy7i-axqbvUYJjHNkL4JtOwglVXeUAPcvGVnV4aApKsEALw_wcB&gclsrc=aw.ds)
- Filterable Popular Articles (Today, This past week or This past month)
- Shared Element Transitons

## Methodology

My go-to approach (for years) has bee MVP (Model-View-View-Presenter ), with as little processing logic as possible applied to Activities

- [Models](https://github.com/febrianasahara/ikagengm-api-assessment/tree/main/api_lib) - Can be found in app_lib project
- [Views](https://github.com/febrianasahara/ikagengm-api-assessment/tree/main/app/src/main/java/com/ikymasie/ny_times_api_assessment/views) - Can be found in the app/src directory
- [Presenters](https://github.com/febrianasahara/ikagengm-api-assessment/tree/main/app/src/main/java/com/ikymasie/ny_times_api_assessment/presenters) - commonly known as Adapters


## App Installation
 **From Google Play**
- click [this Link](https://play.google.com/store/apps/details?id=com.ikymasie.ny_times_api_assessment) to take you to the **Google Play Store**
- select **'Install'** and wait for the app to be installed on your device
- Open the App and Test as you wish

**From App Storage (.apk)**

- click [this Download Link](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/app-release.apk?alt=media&token=c3de9809-9823-4360-9cc5-4998f698c326) and wait for the App to you device
- Select the download file and select "Install"
- If prompted **Allow "Install from Unknown Sources"
- Open the App

## Running The Code
#### Prerequisites ####
- Android Studio (latest)
- Android Emulator/Physical Android Device (API 26 - API 28)
- Internet
- Git knowledge

#### How To Run ####
1. **Open Android Studio**
2. Select *File > Open*
3. Select the App Folder you cloned the Source Code
4. Allow android graddle to **download dependencies**
5. Select *Build > Rebuild* Project (in the Android navigaton menu)
6. Select *'app'* in your **build configuration** options ![image1](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/android_image_1.png?alt=media&token=012c20fa-2bf0-4ded-bf2b-963c59896024)
7. Select the **appropriate device/Emulator**
8. Press the green **'PLAY'** button ![play](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/Screenshot%202021-07-21%20at%2003.40.36.png?alt=media&token=598e34e4-d017-4192-86a5-9b207a2865d9) available in your Android Studio
9. Test the App as you wish

# Testing/Coverage
Note: I've chosen to go with **Robolectric** for my **Unit test** because it provides tthe comprehensive UI and logic Mocking i need for the best possible code coverage for my chosen design approach. For my **UI Tests**.
##### Test Suites ####
Used in the submission of this assignmennt
| Test Suite | Used For |
| ------ | ------ |
| [Robolectric](http://robolectric.org/) | [Unit Tests] |
| [Android Espresso](https://developer.android.com/training/testing/espresso) | [UI Tests] |

**NOTE:** I used Espresso (test file can be found under the app>src/**androidTest**/java/com/ikymasie/ny_times_api_assessment  Foler)

![coverage](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/assessment_coverage_report.png?alt=media&token=42f4a564-53af-4d43-b491-3944e32420c4)

#### Running Unit Tests ####
1. Navigate to app > src > test > java > com > ikymasie in Android Studio
2. Right Click the **'ny_times_assessment'** directory
    * **Running Individual Tests**: *Right click* any sub directory file ![alt](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/rrunn_with_coverage.png?alt=media&token=cbab3855-8f17-4fa7-9f6f-6bf319758719) and select ***Run [TestCase] with Coverage***
    * **Running Full Test Suite**:  *Right click* the **MAIN SOURCE DIRECTORY** directory ![alt](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/rrunn_with_coverage.png?alt=media&token=cbab3855-8f17-4fa7-9f6f-6bf319758719) and select ***Run [TestCase] with Coverage***

**Note** : If you're having issues running the Full Test Case, you need to add the following line to your build configuration ***VM Options***
>> -noverify
1. Select 'Edit Configurations' ![build](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/build_fix.png?alt=media&token=db9537b4-0826-4ed4-98ad-3625bdb05502) and select the test configuration you want to edit
2. Enter -noverify under uour VM Options ![alt](https://karl-park.github.io/static/assets/img/posts/robolectric-testcoverage/configuration.png)
3. Apply Changes and Re-Run the est with Coverage

##### Generating Coverage Report #####
***NB*** *: Ensure you have followed the setups highlighted above in Running Unit Tests > Running Full Test Suite*
1. Select Run > Generate Coverage Report from the Android Studio navigation menu ![als](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/coverage_report.png?alt=media&token=704bca87-cbfc-4f2a-a667-d7cba572b453)
2. If asked where you like to save the report(index.html), select the empty directory of your choosing.
3. Click done and review the report

**Below is the last coverage report ran (timestamp attached):**
![full](https://firebasestorage.googleapis.com/v0/b/ny-times-assessment.appspot.com/o/full_coverage.png?alt=media&token=74c5a14a-a495-4001-adfc-1e2cdda13e7f)


## Thank you for your time!
**I Hope this is enough to impress on you how keen i am to Work with your organization.**

