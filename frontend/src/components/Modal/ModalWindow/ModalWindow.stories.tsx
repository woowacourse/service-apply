import { ComponentMeta, ComponentStory } from "@storybook/react";
import useModalContext from "../../../hooks/useModalContext";
import Button, { BUTTON_VARIANT } from "../../@common/Button/Button";
import ModalWindow from "./ModalWindow";

export default {
  title: "components/ModalWindow",
  component: ModalWindow,
} as ComponentMeta<typeof ModalWindow>;

const Template: ComponentStory<typeof ModalWindow> = (args) => {
  const { Modal, openModal } = useModalContext();

  return (
    <div style={{ height: "200vh", backgroundColor: "white", padding: "1rem" }}>
      <Button
        type="button"
        variant={BUTTON_VARIANT.CONTAINED}
        cancel={false}
        className
        onClick={openModal}
      >
        open modal
      </Button>
      <Modal>
        <ModalWindow {...args} />
      </Modal>
    </div>
  );
};

export const Default = Template.bind({});

Default.args = {};

export const LongContent = Template.bind({});

LongContent.args = {
  children: (
    <>
      <p>
        대통령은 국무회의의 의장이 되고, 국무총리는 부의장이 된다. 군인·군무원·경찰공무원 기타
        법률이 정하는 자가 전투·훈련등 직무집행과 관련하여 받은 손해에 대하여는 법률이 정하는
        보상외에 국가 또는 공공단체에 공무원의 직무상 불법행위로 인한 배상은 청구할 수 없다.
        국가원로자문회의의 의장은 직전대통령이 된다. 다만, 직전대통령이 없을 때에는 대통령이
        지명한다. 훈장등의 영전은 이를 받은 자에게만 효력이 있고, 어떠한 특권도 이에 따르지
        아니한다. 국정감사 및 조사에 관한 절차 기타 필요한 사항은 법률로 정한다. 국가는 모성의
        보호를 위하여 노력하여야 한다. 대법원장과 대법관이 아닌 법관의 임기는 10년으로 하며, 법률이
        정하는 바에 의하여 연임할 수 있다.
      </p>

      <p>
        대통령은 법률안의 일부에 대하여 또는 법률안을 수정하여 재의를 요구할 수 없다.
        저작자·발명가·과학기술자와 예술가의 권리는 법률로써 보호한다. 모든 국민은 근로의 의무를
        진다. 국가는 근로의 의무의 내용과 조건을 민주주의원칙에 따라 법률로 정한다. 제1항의
        해임건의는 국회재적의원 3분의 1 이상의 발의에 의하여 국회재적의원 과반수의 찬성이 있어야
        한다. 국가는 농·어민과 중소기업의 자조조직을 육성하여야 하며, 그 자율적 활동과 발전을
        보장한다. 한 회계연도를 넘어 계속하여 지출할 필요가 있을 때에는 정부는 연한을 정하여
        계속비로서 국회의 의결을 얻어야 한다. 근로조건의 기준은 인간의 존엄성을 보장하도록 법률로
        정한다.
      </p>
    </>
  ),
};
