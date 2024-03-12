export type Member = {
  id: number;
  name: string;
  email: `${string}@${string}`;
  phoneNumber: string;
  gender: "MALE" | "FEMALE";
  birthday: Date;
  password: string;
};
