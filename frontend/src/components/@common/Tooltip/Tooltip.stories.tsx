import { ComponentMeta, ComponentStory } from "@storybook/react";
import { MY_MISSION_TOOLTIP_MESSAGE } from "../../../constants/messages";
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

const Template: ComponentStory<typeof Tooltip> = () => (
  <Tooltip tooltipId="" messages={MY_MISSION_TOOLTIP_MESSAGE} />
);

export const Default = Template.bind({});
Default.args = {};
