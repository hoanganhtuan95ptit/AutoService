# 🚀 AutoService for Android Dynamic Feature

This project is built based on my personal experience and may not be fully optimized. I highly welcome feedback from the community. 🙏

## Problem.

I really like Google’s AutoService, as it helps automate many tasks without worrying about the execution location. However, when using AutoService with Dynamic Feature, a problem occurred. ⚠️

In my app, I have a service (interface A), and I want to implement it in different Dynamic Features (A, B, C, …). The goal is for each customer to be configured to use a specific implementation.
But when integrating AutoService into Dynamic Feature, I got the error:

```java
Duplicate META-INF.services
```
This forced me to look for a new solution. 🔍

The question:

```java
How can the App, Module, and Dynamic Feature access each other’s implementations without causing duplication errors? 🤔
```


## 💡Idea

While working with Dynamic Features, I realized that the App, Modules, and Dynamic Features can all easily access data from the assets folder. 🗂️
So I came up with an idea:"

```java
Build a mechanism similar to Google’s AutoService, but load implementations through assets instead of META-INF. ✨
```

## 🛠️Implementation Steps

1. 🏷️ **Create an annotation** and a mechanism to load implementations from assets.

2. 🔧 **Use KAPT** to scan annotated implementations and generate registration files.

3. 📝 **Write a Gradle plugin**:
    - 🔌 Automatically register annotation libraries and KAPT for modules using the plugin.
    - 📁 Specify the folder location for `assets`.

4. 🚀 **Integrate into the project**:
    - 🎉 Fortunately, this idea works! Modules and dynamic features can now access each other’s implementations through assets without duplication errors. ✅
