import dayjs from 'dayjs/esm';
import { ReplyType } from 'app/entities/enumerations/reply-type.model';

export interface ITestEntity {
  id: number;
  stringField?: string | null;
  integerField?: number | null;
  longField?: number | null;
  bigDecimalField?: number | null;
  floatField?: number | null;
  doubleField?: number | null;
  enumField?: ReplyType | null;
  booleanField?: boolean | null;
  localDateField?: dayjs.Dayjs | null;
  zonedDateField?: dayjs.Dayjs | null;
  instantField?: dayjs.Dayjs | null;
  durationField?: string | null;
  uuidField?: string | null;
  blobField?: string | null;
  blobFieldContentType?: string | null;
  anyBlobField?: string | null;
  anyBlobFieldContentType?: string | null;
  imageBlobField?: string | null;
  imageBlobFieldContentType?: string | null;
  textBlobField?: string | null;
}

export type NewTestEntity = Omit<ITestEntity, 'id'> & { id: null };
