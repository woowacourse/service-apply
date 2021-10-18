import { CONTAINER_SIZE } from "../Container/Container";
import Panel from "./Panel";

export default {
  title: "components/Panel",
  component: Panel,
};

const Template = (args) => <Panel {...args}>본문</Panel>;

export const Default = Template.bind({});

Default.args = {
  title: "웹 백엔드 4기",
  size: CONTAINER_SIZE.DEFAULT,
};
