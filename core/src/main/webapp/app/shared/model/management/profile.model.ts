export interface IProfile {
  id?: string;
  userId?: string | null;
  descreption?: string | null;
  phoneNumber?: string | null;
  city?: string | null;
  profilePictureContentType?: string | null;
  profilePicture?: string | null;
  accountType?: string | null;
}

export const defaultValue: Readonly<IProfile> = {};
