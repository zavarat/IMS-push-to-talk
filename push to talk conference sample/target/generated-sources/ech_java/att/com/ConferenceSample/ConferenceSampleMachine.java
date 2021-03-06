//
// This file was generated by ech2java. Do not modify.
//

// ECharts source top line tag: 7
package att.com.ConferenceSample;
// ECharts source bottom line tag: 7

import org.echarts.*;
import org.echarts.monitor.*;
// ECharts source top line tag: 9
import  java.util.Properties;
// ECharts source bottom line tag: 9
// ECharts source top line tag: 10
import  javax.servlet.ServletContext;
// ECharts source bottom line tag: 10
// ECharts source top line tag: 11
import  javax.servlet.sip.*;
// ECharts source bottom line tag: 11
// ECharts source top line tag: 12
import  javax.servlet.sip.ar.*;
// ECharts source bottom line tag: 12
// ECharts source top line tag: 13
import  org.echarts.servlet.sip.*;
// ECharts source bottom line tag: 13
// ECharts source top line tag: 14
import  org.echarts.servlet.sip.messages.*;
// ECharts source bottom line tag: 14
// ECharts source top line tag: 15
import  org.echarts.servlet.sip.machines.*;
// ECharts source bottom line tag: 15
// ECharts source top line tag: 16
import  att.com.ConferenceSample.*;
// ECharts source bottom line tag: 16
// ECharts source top line tag: 17
import  org.echarts.*;
// ECharts source bottom line tag: 17
// ECharts source top line tag: 18
import  messages.sipToJavaNotification;
// ECharts source bottom line tag: 18

/**
 * Represents Invite Handler that creates a Conference FSM and route each Invite to it.
 * Since it's a bounded box, it allows one instance.
 */
public class ConferenceSampleMachine extends TransitionMachine implements ConferenceSampleControl, BoundBoxMachine {
// Static declarations for ConferenceSampleMachine
private static final int ConferenceSampleMachine_NUM_STATES = 3;
private static State[] ConferenceSampleMachine_states = new State[ConferenceSampleMachine_NUM_STATES];
private static void initialize_ConferenceSampleMachine_states_0() {
ConferenceSampleMachine_states[0] = new OrState("INIT", BasicMachineConstructor.BASIC_MACHINE_CONSTRUCTOR, null, null, false);
ConferenceSampleMachine_states[1] = new OrState("CREATE_CONFERENCE", new MachineConstructor() { public Machine newInstance(final Machine parentMachine, final int machineIndex, final MachineCode machineCode) throws Exception { return ((ConferenceSampleMachine) parentMachine).state_CREATE_CONFERENCE(parentMachine, machineIndex, machineCode); }}, null, null, false);
ConferenceSampleMachine_states[2] = new OrState("END", BasicMachineConstructor.BASIC_MACHINE_CONSTRUCTOR, null, null, false);
}
static {
    initialize_ConferenceSampleMachine_states_0();
}
private static MachineMessageTransitions[] ConferenceSampleMachine_messageTransitions = initializeMessageTransitions(new MachineMessageTransitions[ConferenceSampleMachine_NUM_STATES]);
private static MachineMessagelessTransitions[] ConferenceSampleMachine_messagelessTransitions = initializeMessagelessTransitions(new MachineMessagelessTransitions[ConferenceSampleMachine_NUM_STATES]);
private static void initialize_ConferenceSampleMachine_transitions_0() {
addMessageTransition(ConferenceSampleMachine_messageTransitions, new MessageTransition(new PortMethod() { public LocalPort invoke(final Machine machine) { return ((ConferenceSampleMachine) machine).transition_1_port(); }}, Invite.class, new TransitionSource(new MultiStateConfiguration(0, new StateConfiguration[]{Machine.BASIC_CONFIG, Machine.VARIABLE_CONFIG, Machine.VARIABLE_CONFIG}, new boolean[] {false, false, false}, new int[] {0, -1, -1}, null), "[INIT]"), new CompoundTransitionTarget(null, new TransitionTarget[]{new BasicTransitionTarget(new MultiStateConfiguration(1, new StateConfiguration[]{Machine.VARIABLE_CONFIG, Machine.BASIC_CONFIG, Machine.VARIABLE_CONFIG}, new boolean[] {false, false, false}, new int[] {-1, 0, -1}, new SubmachineBindingGettor() { public final Machine getSubmachine(final int cfgIndex, final Machine machine) throws Exception { return ((ConferenceSampleMachine) machine).get_transition_1_1_tgt_machine_binding(cfgIndex); }}), "[CREATE_CONFERENCE]", new MessageGuardMethod() { public boolean invoke(final Machine machine, final LocalPort port, final Object message) throws Exception { return ((ConferenceSampleMachine) machine).transition_1_1_guard(port, (Invite) message); }}, new MessageActionMethod() { public void invoke(final Machine machine, final LocalPort port, final Object message) throws Exception { ((ConferenceSampleMachine) machine).transition_1_1_action(port, (Invite) message); }})}), true), 0);
addMessageTransition(ConferenceSampleMachine_messageTransitions, new MessageTransition(new PortMethod() { public LocalPort invoke(final Machine machine) { return ((ConferenceSampleMachine) machine).transition_2_port(); }}, Invite.class, new TransitionSource(new MultiStateConfiguration(1, new StateConfiguration[]{Machine.VARIABLE_CONFIG, Machine.BASIC_CONFIG, Machine.VARIABLE_CONFIG}, new boolean[] {false, false, false}, new int[] {-1, 0, -1}, null), "[CREATE_CONFERENCE]"), new CompoundTransitionTarget(null, new TransitionTarget[]{new BasicTransitionTarget(Machine.DEEP_HISTORY_CONFIG, "[DEEP_HISTORY]", new MessageGuardMethod() { public boolean invoke(final Machine machine, final LocalPort port, final Object message) throws Exception { return ((ConferenceSampleMachine) machine).transition_2_1_guard(port, (Invite) message); }}, new MessageActionMethod() { public void invoke(final Machine machine, final LocalPort port, final Object message) throws Exception { ((ConferenceSampleMachine) machine).transition_2_1_action(port, (Invite) message); }})}), true), 1);
}
static {
    initialize_ConferenceSampleMachine_transitions_0();
}
// Declarations for ConferenceSampleMachine
// ECharts source top line tag: 42
public ConferenceSampleMachine(FeatureBox box, Properties servletProps, ServletContext servletContext, final Machine parentMachine, final int machineIndex, final MachineCode machineCode) throws Exception {
super(ConferenceSampleMachine_states, ConferenceSampleMachine_messageTransitions, ConferenceSampleMachine_messagelessTransitions, ConferenceSampleMachine.class.getName(), parentMachine, machineIndex, machineCode);
this.box            = box;
		this.servletProps   = servletProps;
		this.servletContext = servletContext;

		boxPort = box.getBoxPort();

		// To use the EChartsMachineToJava mechanism, create a class named
		// ConferenceSampleMachineToJavaImpl that implements ConferenceSampleMachineToJava
		// and uncomment the line below.  You can use a different class name
		// by specifying the sipToJavaClassName init-param in sip.xml.
		//
		
		// TODO use the sessionKey as conference name, (we already extract it in sipServlet);
}

public ConferenceSampleMachine(FeatureBox box, Properties servletProps, ServletContext servletContext) throws Exception {
this(box, servletProps, servletContext, null, -1, null);
}
// ECharts source bottom line tag: 42

// ECharts source top line tag: 25

	FeatureBox     box;
	Properties     servletProps;
	ServletContext servletContext;
	BoxPort boxPort;
	/** Initated only if a new invite was recieved from the user**/
	SipPort caller;
	
	private SipServletRequest initialInvite = null;
	
	private InternalPort externalPortServer = null;
	
	private String conferenceName = null;
							  
	sipToJavaNotification sipToJaveNotify;
;
// ECharts source bottom line tag: 25
// ECharts source top line tag: 62
 ConferenceFSM CREATE_CONFERENCE = null;
public Machine state_CREATE_CONFERENCE(final Machine parentMachine, final int machineIndex, final MachineCode machineCode) throws Exception {
CREATE_CONFERENCE = new ConferenceFSM(box, servletProps, servletContext, externalPortServer, initialInvite, sipToJaveNotify, parentMachine, machineIndex, machineCode);
return CREATE_CONFERENCE;
}
// ECharts source bottom line tag: 62
final protected void clearSubmachineReference(final int index) {
switch(index) {
case 1: CREATE_CONFERENCE = null; break;
default: break;
}
}
// ECharts source top line tag: 64
public LocalPort transition_1_port() {
return boxPort;
}

final private Machine get_transition_1_1_tgt_machine_binding(final int cfgIndex) throws Exception {
	switch (cfgIndex) {

    default:
        throw new MachineException("No subconfiguration machine binding defined for subconfiguration index " + cfgIndex);
	}
}
public boolean transition_1_1_guard(final LocalPort port, final Invite message) throws Exception {
return (externalPortServer) == (null);
}
public void transition_1_1_action(final LocalPort port, final Invite message) throws Exception {
initialInvite = message;		
		externalPortServer = new InternalPort(this,"server");
		conferenceName = EChartsSipServlet.sipApplicationKey(message);
		ConferenceSampleMachineToJava toJava = EChartsMachineToJava.getInstance(box.getApplicationSession());
		sipToJaveNotify = new sipToJavaNotification(conferenceName, toJava);;
putEvent(new DebugEvent(("ConferenceSampleMachine Got Invite") + (message)));
;
}
// ECharts source bottom line tag: 64
// ECharts source top line tag: 77
public LocalPort transition_2_port() {
return boxPort;
}

final private Machine get_transition_2_1_tgt_machine_binding(final int cfgIndex) throws Exception {
	switch (cfgIndex) {

    default:
        throw new MachineException("No subconfiguration machine binding defined for subconfiguration index " + cfgIndex);
	}
}
public boolean transition_2_1_guard(final LocalPort port, final Invite message) throws Exception {
return (externalPortServer) != (null);
}
public void transition_2_1_action(final LocalPort port, final Invite message) throws Exception {
putEvent(new DebugEvent(("THE PEER is ") + (message)));
externalPortServer.output(message, this);
;
}
// ECharts source bottom line tag: 77
}
