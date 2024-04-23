import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';
import { writePdf } from './utility/utils';

async function main() {

    const argv = await yargs(hideBin(process.argv))
        .usage('Usage -i <input> -o <output>')
        .option('input', {
            alias: 'i',
            type: 'string',
            demandOption: true,
            describe: 'input file',
        })
        .option('output', {
            alias: 'o',
            type: 'string',
            demandOption: true,
            describe: 'output file',
        }).argv;

    const inputPath = argv.input;
    const outputPath = argv.output;
    console.log('input: ', inputPath);
    console.log('output: ', outputPath);
    await writePdf(inputPath, outputPath);
};

main();

