# Introduction #
Stab is a general-purpose, multi-paradigm programming language for the Java Virtual Machine, inspired by C# in its syntax and ecosystem.

# Design Goals #
The goals used to design Stab were as follows:
  * The syntax should be readable without difficulties by Java/C# programmers.
  * The execution of a Stab program should be as efficient as the execution of the same program written in Java.
  * The generated bytecode should be usable transparently from Java.
  * The Java libraries should be usable from Stab programs without adaptations.

# Current State #
The compiler is currently available as a beta version, but it is stable enough to compile its own source code (Stab is written 100% in Stab).
Language modifications may occur in new versions.

# Download #
The latest Java binaries can be downloaded from the [releases](//github.com/IllidanS4/stab-language/releases) section.

The Stab language support consists of 3 archives:
  * Compiler (`stabc.jar`) – necessary to build Stab code (requires [`asm-8.0.1.jar`](https://repo1.maven.org/maven2/org/ow2/asm/asm/8.0.1/asm-8.0.1.jar)).
  * Runtime (`stabrt.jar`) – contains classes, interfaces, and annotations used by the Stab compiler. Becomes a dependency if used in code, even indirectly.
  * Annotated libraries (`stabal.jar`) – provides additional annotations for external Java libraries for use by the compiler. Does not become a dependency.

# Building #
GNU `make` can be used to build the whole project.
When used in the root directory, it will automatically download the latest supported version of the compiler and start compiling all archives.
Alternatively, you can use `make prepare` to only download the necessary archives, and proceed to individual subdirectories.

You can simply use `make` in `compiler`, `runtime`, `annotated`, and `eclipse` to build those projects.