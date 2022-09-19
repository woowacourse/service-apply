import { ComponentMeta, ComponentStory } from "@storybook/react";
import HelpPanel from "./HelpPanel";

export default {
  title: "components/HelpPanel",
  component: HelpPanel,
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
} as ComponentMeta<typeof HelpPanel>;

const Template: ComponentStory<typeof HelpPanel> = () => <HelpPanel />;

export const Default = Template.bind({});
Default.args = {};
