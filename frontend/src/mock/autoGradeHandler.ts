import { rest } from "msw";
import { judgmentDummy, missionsDummy, myApplicationDummy, recruitmentDummy } from "./dummy";

const MOCK_API_BASE_URL = `http://localhost:8080/api`;

export const autoGradeHandler = [
  rest.get(`${MOCK_API_BASE_URL}/application-forms/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ message: "", body: myApplicationDummy }));
  }),
  rest.get(`${MOCK_API_BASE_URL}/recruitments`, (req, res, ctx) => {
    return res(ctx.json({ message: "", body: recruitmentDummy }));
  }),
  rest.get(`${MOCK_API_BASE_URL}/recruitments/:recruitmentId/missions/me`, (req, res, ctx) => {
    const recruitmentId = req.params.recruitmentId as "1" | "2";

    return res(ctx.json({ message: "", body: missionsDummy[recruitmentId] }));
  }),
  rest.get(
    `${MOCK_API_BASE_URL}/recruitments/:recruitmentId/missions/:missionId/judgment/judge-example`,
    (req, res, ctx) => {
      const mockedMissionJudgment = { ...judgmentDummy };

      return res(ctx.json({ message: "", body: mockedMissionJudgment }));
    }
  ),
  rest.post(
    `${MOCK_API_BASE_URL}/recruitments/:recruitmentId/missions/:missionId/judgment/judge-example`,
    (req, res, ctx) => {
      const mockedMissionJudgment = { ...judgmentDummy };

      return res(ctx.json({ message: "", body: mockedMissionJudgment }));
    }
  ),
];
