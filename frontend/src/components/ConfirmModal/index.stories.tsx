import { useReducer } from 'react';

import ConfirmModal from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/ConfirmModal',
  component: ConfirmModal,
} as ComponentMeta<typeof ConfirmModal>;

const Template: ComponentStory<typeof ConfirmModal> = args => {
  const [isConfirmModalOpen, handleConfirmModal] = useReducer(state => !state, true);

  return (
    <>{isConfirmModalOpen && <ConfirmModal {...args} handleCancel={handleConfirmModal} handleConfirm={() => {}} />}</>
  );
};

export const ConfirmModalTemplate: ComponentStory<typeof ConfirmModal> = Template.bind({});
ConfirmModalTemplate.args = {
  title: '삭제',
  notice: '해당 글을 삭제하시겠습니까?',
};
