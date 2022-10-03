import { rest } from "msw";
import { API_BASE_URL } from "../../../.storybook/preview";
import { missionsDummy, myApplicationDummy } from "../../mock/dummy";
import MyApplication from "./MyApplication";
import { MemoryRouter } from "react-router-dom";

export default {
  title: "pages/MyApplication",
  component: MyApplication,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/applications/me",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <MyApplication {...args} />;

export const Default = Template.bind({});
Default.parameters = {
  msw: [
    rest.get(`${API_BASE_URL}/api/application-forms/me`, (req, res, ctx) => {
      return res(ctx.json({ message: "", body: myApplicationDummy }));
    }),
    rest.get(
      `${API_BASE_URL}/api/recruitments/${myApplicationDummy[0].recruitmentId}/missions/me`,
      (req, res, ctx) => {
        return res(ctx.json({ message: "", body: [] }));
      }
    ),
    rest.get(`${API_BASE_URL}/api/recruitments/:recruitmentId/missions/me`, (req, res, ctx) => {
      return res(ctx.json({ message: "", body: missionsDummy }));
    }),
  ],
};
