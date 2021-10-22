import AssignmentSubmit from "./AssignmentSubmit";
import { Route, MemoryRouter } from "react-router-dom";
import { missionDummy } from "../../mock/dummy";

export default {
  title: "pages/AssignmentSubmit",
  component: AssignmentSubmit,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/assignment/new",
            state: {
              recruitmentId: 1,
              currentMission: missionDummy[0],
            },
          },
        ]}
      >
        <Route path="/assignment/:status">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <AssignmentSubmit {...args} />;

export const Default = Template.bind({});
