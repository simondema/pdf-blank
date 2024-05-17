# Typescript

A simple utility written in NodeJS to add a blank page after each page in the PDF file received as input. Using pdf-lib.
It could be useful to take side-notes on the PDF file if printed in 2x3 format.

## Install

Install the utility by running the command:

```sh
npm run build && npm install -g .
```

## Usage

Use the utility following the usage instruction, running the command:

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
5. Install the dependencies and regenerate **package-lock.json** by running:

```sh
npm install
```
6. Build the project by running:

```sh
npm run build
```
7. Install or update the module in your local machine, running:

```sh
npm install -g .
```