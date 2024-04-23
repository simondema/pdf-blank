# PdfBlank

A simple utility written in NodeJS to add a blank page after each page in the PDF file received as input.
It could be useful to take side-notes on the PDF file if printed in 2x3 format.

## Install

To install the utility, run the command:

```sh
npm run build && npm install -g .
```

## Usage

To use the utility, follow the usage instruction running the command:

```sh
pb -i <inputPath> -o <outputPath>
```

## Developers

For developer usage:

1. Install [**rimraf**](https://github.com/isaacs/rimraf) running the command

```sh
npm install -g rimraf
```

2. To clean the dist folder using **rimraf** tool installed at the previous step, run the command

```sh
npm run clean
```

3. Do any changes you want
4. Remove **package-lock.json** file if present at the root of the project
5. Run the command 

```sh
npm install
```
6. Run the command

```sh
npm run build
```
7. To install or update the module in your local machine, run the command

```sh
npm install -g .
```