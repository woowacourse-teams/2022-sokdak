import { useReducer } from 'react';

import ReportModal from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/ReportModal',
  component: ReportModal,
} as ComponentMeta<typeof ReportModal>;

const Template: ComponentStory<typeof ReportModal> = () => {
  const [isReportModalOpen, handleReportModal] = useReducer(state => !state, true);

  return <ReportModal isModalOpen={isReportModalOpen} onClose={handleReportModal} />;
};

export const ReportModalTemplate: ComponentStory<typeof ReportModal> = Template.bind({});
ReportModalTemplate.bind({
  isModalOpen: true,
});
