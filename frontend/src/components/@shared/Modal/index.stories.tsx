import { useReducer } from 'react';

import Modal from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Modal',
  component: Modal,
} as ComponentMeta<typeof Modal>;

const Template: ComponentStory<typeof Modal> = args => {
  const [isModalOpen, handleModal] = useReducer(state => !state, true);

  return (
    <Modal {...args} handleCancel={handleModal} isModalOpen={isModalOpen}>
      <div style={{ width: '400px', height: '300px', backgroundColor: 'white' }}>hi</div>
    </Modal>
  );
};

export const ModalTemplate: ComponentStory<typeof Modal> = Template.bind({});
