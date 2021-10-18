import React from "react";
import { Route, MemoryRouter } from "react-router-dom";
import Join from "./Join";

export default {
  title: "pages/Join",
  component: Join,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/applicants/new",
            state: {
              recruitmentId: 3,
            },
          },
        ]}
      >
        <Route path="/applicants/new">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <Join />;

export const Default = Template.bind({});
