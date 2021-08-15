import React from "react";
import { rest } from "msw";
import { Route, MemoryRouter } from "react-router-dom";

import ApplicationRegister from "./ApplicationRegister";
import { API_BASE_URL } from "../../../.storybook/preview";

export default {
  title: "pages/ApplicationRegister",
  component: ApplicationRegister,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/application-forms/new",
            search: "?recruitmentId=3",
          },
        ]}
      >
        <Route path="/application-forms/:status">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <ApplicationRegister {...args} />;

export const Default = Template.bind({});
Default.parameters = {
  msw: [
    rest.get(
      `${API_BASE_URL}/api/recruitments/:recruitmentId/items`,
      (req, res, ctx) => {
        return res(
          ctx.json({
            message: "",
            body: [
              {
                recruitmentId: 1,
                title: "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?",
                position: 1,
                maximumLength: 1000,
                description:
                  "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
                id: 1,
              },
              {
                recruitmentId: 1,
                title: "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?",
                position: 1,
                maximumLength: 1000,
                description:
                  "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
                id: 2,
              },
            ],
          })
        );
      }
    ),
    rest.post(`${API_BASE_URL}/api/application-forms`, (req, res, ctx) => {
      return res(ctx.json({}));
    }),
  ],
};
