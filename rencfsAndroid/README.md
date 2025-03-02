Cargo Android Build notes:
1. Add android targets:
rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android x86_64-linux-android i686-linux-android

2. Cargo Android Build needs to finds the python executable in PATH, alias python to python3:
sudo ln -s "$(which python3)" /usr/bin/python
