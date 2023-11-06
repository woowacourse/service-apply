import { MemoryRouter } from "react-router-dom";
import { MISSION_STATUS, MISSION_SUBMISSION_METHOD } from "../../constants/recruitment";

import AssignmentSubmit from "./AssignmentSubmit";

const withMemoryRouterDecorator = (Story, { args }) => {
  const initialState = {
    recruitmentId: 1,
    currentMission: {
      id: 1,
      title: "과제제출 기간 전",
      description: "설명",
      startDateTime: "2020-10-25T15:00:00",
      endDateTime: "2020-11-25T15:00:00",
      submitted: false,
      submittable: true,
      status: MISSION_STATUS.SUBMITTABLE,
      runnable: true,
      judgment: null,
      ...args.state.currentMission,
    },
  };

  return (
    <MemoryRouter
      initialEntries={[
        {
          pathname: "/assignment/new",
          state: initialState,
        },
      ]}
    >
      <Story />
    </MemoryRouter>
  );
};

export default {
  title: "pages/AssignmentSubmit",
  component: AssignmentSubmit,
  decorators: [withMemoryRouterDecorator],
};

const Template = (args) => <AssignmentSubmit {...args} />;

export const SubmissionMethodPublicPullRequest = Template.bind({});
SubmissionMethodPublicPullRequest.args = {
  state: {
    currentMission: {
      submissionMethod: MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST,
    },
  },
};

export const SubmissionMethodPrivateRepository = Template.bind({});
SubmissionMethodPrivateRepository.args = {
  state: {
    currentMission: {
      submissionMethod: MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY,
    },
  },
};
