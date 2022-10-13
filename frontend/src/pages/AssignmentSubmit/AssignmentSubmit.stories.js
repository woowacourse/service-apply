import { MemoryRouter } from "react-router-dom";
import { missionsDummy } from "../../mock/dummy";
import AssignmentSubmit from "./AssignmentSubmit";

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
              currentMission: missionsDummy[0],
            },
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <AssignmentSubmit {...args} />;

export const Default = Template.bind({});
