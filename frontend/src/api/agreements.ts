import axios from "axios";

export type AgreementResponse = {
  id: number;
  version: number;
  content: string;
};

export const fetchAgreement = () => axios.get<AgreementResponse>("/api/agreements/latest");
