# QTVidArrange
Arranges h.264 video files in subfolders named by image resolution and frames per second.

#### Requirements

Requires the [QTReader library](https://github.com/lpcsmath/QTReader).


#### Usage

Show a list of video files with their meta data:

```
java -jar qt-vid-arrange-<version>.jar show [dir]
```

Create subfolders and create symbolic links in them:

```
java -jar qt-vid-arrange-<version>.jar link [source_dir [target_dir]]
```

Create subfolders and move the files into them:

```
java -jar qt-vid-arrange-<version>.jar move [source_dir [target_dir]]
```

Create subfolders and copy the files into them:

```
java -jar qt-vid-arrange-<version>.jar copy [source_dir [target_dir]]
```

#### Example

Shows a list of files:

```
$ java -jar qt-vid-arrange-1.0-SNAPSHOT.jar show
DURATION  WIDTH  HEIGHT  FRAMESPSEC  CREADATE             FILE
    1:57   3840    2160       24.00  2016-09-11 12:04:46  XT210107.MOV
    0:20   3840    2160       24.00  2016-09-14 09:04:04  XT210133.MOV
    0:18   3840    2160       24.00  2016-09-16 11:42:53  XT210138.MOV
```