# PdfBlank

A simple utility written in Java to perform pdf manipulation, using Apache PDFBox 3

## Packaging

Package the executable by running the command:

```sh
mvn clean package
```

## Usage

Use the utility following the usage instruction, running the command:

```sh
java -jar <jar_file> -i <input_file_path> -o <output_file_path> -op <operation>
```

The operation type can be
```md
- blank -> will add a blank page after each page in the pdf
- merge -> will perform the blank operation plus merging the slides in a A4 page with 3x2 format
- annotate -> will perform the merge operation plus adding a Annotation box on the right side of each A4 page
```