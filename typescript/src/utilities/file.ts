import * as fs from 'fs';
import { logger } from './logger';

export function deleteFile(path: string): void {
    fs.unlinkSync(path);
    logger.info('File deleted successfully');
}

