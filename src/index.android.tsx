import React, { useCallback } from 'react';
import {
  StyleSheet,
  TouchableWithoutFeedback,
  View,
  requireNativeComponent,
  Modal,
  Animated,
  type HostComponent,
  type NativeSyntheticEvent,
  type ViewProps,
} from 'react-native';
import {
  PullUpPropTypes,
  PullUpDefaultProps,
  type PullUpProps,
  type SheetState,
} from './types';

/* Props for Native Android component.
 * Not to be confused with the main component. */
interface NativeProps extends ViewProps {
  state: SheetState;
  collapsedHeight?: number;
  maxSheetWidth?: number;
  hideable?: boolean;
  onStateChanged: (evt: NativeSyntheticEvent<{ state: SheetState }>) => void;
}

const NativePullUp: HostComponent<NativeProps> = requireNativeComponent(
  'RNPullUpView'
);

const styles = StyleSheet.create({
  primary: {
    position: 'absolute',
    bottom: 0,
    width: '100%',
    justifyContent: 'flex-end',
  },
  sheet: {
    flex: 1,
    backgroundColor: 'white',
  },
  modal: {
    position: 'absolute',
  },
  overlay: {
    flex: 1,
    backgroundColor: PullUpDefaultProps.overlayColor,
  },
});

const PullUpBase = (props: PullUpProps) => {
  const {
    collapsedHeight,
    maxSheetWidth,
    onStateChanged,
    style,
    children,
  } = props;

  const onNativeStateChanged = useCallback(
    (evt: NativeSyntheticEvent<{ state: SheetState }>) => {
      onStateChanged?.(evt.nativeEvent.state);
    },
    [onStateChanged]
  );

  const maxWidthStyle =
    typeof maxSheetWidth === 'number' && maxSheetWidth > 0
      ? { maxWidth: maxSheetWidth }
      : null;

  return (
    <NativePullUp
      {...props}
      style={styles.primary}
      collapsedHeight={collapsedHeight || 0}
      maxSheetWidth={maxSheetWidth || 0}
      onStateChanged={onNativeStateChanged}
    >
      <View collapsable={false} style={[styles.sheet, style, maxWidthStyle]}>
        {children}
      </View>
    </NativePullUp>
  );
};

class PullUpModal extends React.Component<PullUpProps> {
  opacity: Animated.Value;
  state: { destroyed: boolean; animating: 'in' | 'out' | false };

  constructor(props: PullUpProps) {
    super(props);
    this.opacity = new Animated.Value(0);
    this.state = {
      destroyed: true,
      animating: false,
    };
  }

  componentDidMount() {
    const { state } = this.props;
    if (state !== 'hidden') this._animateIn();
  }

  componentDidUpdate(prevProps: PullUpProps) {
    const { state } = this.props;
    const { animating, destroyed } = this.state;
    if (animating || state === prevProps.state) return;

    if (state === 'hidden' && !destroyed) this._animateOut();
    if (state !== 'hidden' && destroyed) this._animateIn();
  }

  _animateOut = () => {
    if (this.state.animating === 'out') return;
    this.setState({ animating: 'out' });
    Animated.timing(this.opacity, {
      toValue: 0,
      duration: 250,
      useNativeDriver: true,
    }).start(({ finished }) => {
      if (finished) this.setState({ destroyed: true, animating: false });
    });
  };

  _animateIn = () => {
    if (this.state.animating === 'in') return;
    this.setState({ animating: 'in' });
    setTimeout(() => this.setState({ destroyed: false }));
    Animated.timing(this.opacity, {
      toValue: this.props.overlayOpacity || PullUpDefaultProps.overlayOpacity,
      duration: 250,
      useNativeDriver: true,
    }).start(({ finished }) => {
      if (finished) this.setState({ animating: false });
    });
  };

  _onRequestClose = () => {
    const { dismissable } = this.props;
    if (dismissable) {
      this._animateOut();
    }
  };

  _onPressOverlay = () => {
    const { dismissable, tapToDismissModal } = this.props;
    if (dismissable && tapToDismissModal) {
      this._animateOut();
    }
  };

  _interceptOnStateChanged = (newState: SheetState) => {
    const { animating } = this.state;
    const { onStateChanged } = this.props;
    if (newState === 'hidden') {
      if (animating === 'in') return;
      if (!animating) {
        this._animateOut();
      }
    }
    onStateChanged?.(newState);
  };

  render() {
    const { state, hideable, dismissable, overlayColor } = this.props;
    const { destroyed, animating } = this.state;

    if (destroyed && !animating) return null;

    return (
      <Modal transparent onRequestClose={this._onRequestClose}>
        <TouchableWithoutFeedback onPress={this._onPressOverlay}>
          <Animated.View style={[styles.overlay, { opacity: this.opacity, backgroundColor: overlayColor }]} />
        </TouchableWithoutFeedback>
        <PullUpBase
          {...this.props}
          state={destroyed || animating === 'out' ? 'hidden' : state}
          onStateChanged={this._interceptOnStateChanged}
          hideable={hideable && dismissable}
        />
      </Modal>
    );
  }
}

const PullUp = (props: PullUpProps) =>
  props.modal ? <PullUpModal {...props} /> : <PullUpBase {...props} />;

PullUp.propTypes = PullUpPropTypes;
PullUp.defaultProps = PullUpDefaultProps;

export default PullUp;
export type { PullUpProps, SheetState };
