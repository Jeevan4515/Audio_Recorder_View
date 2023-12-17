
# Android Audio Recorder View

This recorder works on android 13 and above. Live recorder view is displayed.

## Demo
![Screen_recording_emulator](https://github.com/Jeevan4515/Audio_Recorder_View/assets/68682819/ecb853db-dc27-4818-afdd-35496d4d7a22)


## Note

Do not forget to add permissions in your AndroidManifest.xml 


    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />




## Usage

Ensure you have maven in your project management

~~~
....
repositories{
google()
mavenCentral()
maven( url = uri("https://jitpack.io"))
}
...
~~~

Add dependency in your app level gradle file

```
implementation ("com.github.Jeevan4515:Audio_Recorder_View:1.0.0")
```


In your xml file you can use:

```
...
    <com.jeevan.audiorecorderview.AudioRecorderView
        android:id="@+id/recorder_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
...

```
Something like this

**You can also find in the project for the usage**
## Features

- Live preview
- Playback
- Delete

