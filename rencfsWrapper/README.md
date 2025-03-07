# MacOS Sonoma arm
Make sure cargo is installed and the path is set correctly, check with `cargo --version` and `echo $PATH` in the terminal to verify.
Gradle Daemon (when launched from IntelliJ) inherits the macOS GUI session environment, which does not include paths added in .zshrc
`launchctl getenv PATH` can  be used to check the path in the launchd environment (applies to GUI and gradle sub-processes). 


