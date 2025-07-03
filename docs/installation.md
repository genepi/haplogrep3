# Local Installation

You can either (a) download and install the web service locally or (b) download the command-line interface. Please click [here](https://haplogrep.i-med.ac.at/haplogrep3) in case you want to use our hosted service.

## Requirements

You will need the following things properly installed on your computer.

* Java 8 or higher

Haplogrep works on Linux, macOS and Windows. Please note that fasta input files are currently supported for Linux and macOS only.

## Download and Install

### Linux or macOS

Download and install the latest version from our download page using the following commands:

```
cd my-haplogrep3-install-folder
wget https://github.com/genepi/haplogrep3/releases/download/v3.2.2/haplogrep3-3.2.2-linux.zip
unzip haplogrep3-3.2.2-linux.zip
./haplogrep3
```

### Windows

Download the [latest Windows version](https://github.com/genepi/haplogrep3/releases/download/v3.2.2/haplogrep3-3.2.2-windows.zip) from our download page.

- Open the zip file and extract it content to a folder of choice.

- Open the PowerShell and navigate to your folder where you extracted the \*.exe file.

You are now ready to use Haplogrep by running `haplogrep.exe`.

## Run Haplogrep
The following command starts a web service locally:

```
./haplogrep3 server
```
In case you want to run Haplogrep on the command-line execute the following command:
```
./haplogrep3 classify
```
## Parameters
Click [here](../parameters) to learn about all command-line parameters.
