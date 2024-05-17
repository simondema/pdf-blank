import yargs from "yargs";
import { hideBin } from "yargs/helpers";
import { logger } from "./utilities/logger";
import * as Utilities from "./utilities/pdf";

async function main() {
  const argv = await yargs(hideBin(process.argv))
    .usage("Usage -i <input> -o <output>")
    .option("input", {
      alias: "i",
      type: "string",
      demandOption: true,
      describe: "input file",
    })
    .option("output", {
      alias: "o",
      type: "string",
      demandOption: true,
      describe: "output file",
    })
    .option("multi", {
      alias: "m",
      type: "boolean",
      required: false,
      describe: "multiple layout",
    }).argv;

  const inputPath = argv.input;
  const outputPath = argv.output;
  const multi = argv.multi ?? false;
  logger.info("input: ", inputPath);
  logger.info("output: ", outputPath);
  if (!multi) {
    await Utilities.writeBlankPages(inputPath, outputPath);
  } else {
    await Utilities.writeMultiplePages(inputPath, outputPath);
  }
}

main();
