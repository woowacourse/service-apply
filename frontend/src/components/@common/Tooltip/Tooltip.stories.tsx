import { ComponentMeta, ComponentStory } from "@storybook/react";
import {
  PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
  PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
} from "../../../constants/messages";
import Tooltip from "./Tooltip";

export default {
  title: "components/Tooltip",
  component: Tooltip,
  decorators: [
    (Story) => (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
          width: "100%",
        }}
      >
        <Story />
      </div>
    ),
  ],
} as ComponentMeta<typeof Tooltip>;

const Template: ComponentStory<typeof Tooltip> = (args) => <Tooltip {...args} />;

export const PullRequestSubmittion = Template.bind({});
PullRequestSubmittion.args = {
  tooltipId: "",
  messages: PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE,
};

export const RepositorySubmission = Template.bind({});
RepositorySubmission.args = {
  tooltipId: "",
  messages: PRIVATE_REPOSITORY_TOOLTIP_MESSAGE,
};
