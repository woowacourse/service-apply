import TabItem from "./TabItem";
import { ComponentStory, ComponentMeta } from "@storybook/react";

export default {
  title: "components/TabItem",
  component: TabItem,
} as ComponentMeta<typeof TabItem>;

const Template: ComponentStory<typeof TabItem> = (args) => <TabItem {...args} />;

export const Default = Template.bind({});
Default.args = {
  children: "기본",
  checked: false,
};

export const CheckedTabItem = Template.bind({});
CheckedTabItem.args = {
  children: "선택",
  checked: true,
};
