/**
 *
 */
package de.rpgframework.core;

/**
 * @author prelle
 *
 */
public enum CommandType {

	/**
	 * Detect the type of the given license string
	 * 0=License or activation key
	 * Returns: License or action key object
	 */
	LICENSE_TYPE_DETECT,

	/**
	 * Contact server to create a license
	 * 0=Activation key string
	 * 1=Activation Key object
	 * 2=Name to register
	 * 3=Mailaddress to register
	 * Return:
	 * 0: Success:int 0=License created and mailed, 1=License mailed again to old contact, 2= Invalid key
	 * 1: String  Error Message
	 */
	LICENSE_CREATE,

	/**
	 * Verify the license - check if it exists
	 * 0=RoleplayingSystem, 1=Requested Value
	 */
	LICENSE_VERIFICATION,

	/**
	 * List all available licenses for a given roleplaying system
	 * 0=RoleplayingSystem
	 * Returns: Collection<License>
	 */
	LICENSE_LIST,

	/**
	 * Add a license
	 * 0=License Key
	 * Returns: Object[]
	 *   0 = License
	 */
	LICENSE_ADD,

	/**
	 * Remove a license
	 * 0=License Object
	 */
	LICENSE_REMOVE,

	/**
	 * Load data from a given filesystem. Executed by plugins with the PERSISTENCE feature
	 * 0=RoleplayingSystem, 1=FileSystem
	 * Returns: null
	 */
	LOAD_DATA,

	/**
	 * Read a character from a bytebuffer.
	 * 0=RoleplayingSystem, 1=Bytebuffer
	 * Returns: RuleSpecificCharacterObject
	 */
	DECODE,

	/**
	 * Writes a character into a bytebuffer.
	 * 0=RoleplayingSystem, 1=RuleSpecificCharacterObject
	 * Returns: ByteBuffer
	 */
	ENCODE,

	/**
	 * Returns the supported print options
	 * 0=RoleplayingSystem,
	 * 1=RuleSpecificCharacterObject
	 * 2=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame
	 * 3=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions
	 * Returns: Object-Array: 0=PrintType[], 1=Collection<ConfigOpton>
	 */
	PRINT_GET_OPTIONS,

	/**
	 * Returns the supported elements
	 * 0=RoleplayingSystem,<br/>
	 * 1=RuleSpecificCharacterObject<br/>
	 * 2=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame<br/>
	 * 3=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions<br/>
	 * Returns: List of PDFPrintElement
	 */
	PRINT_GET_ELEMENTS,

	/**
	 * Prints the character into the the requested format
	 * 0=RoleplayingSystem,</br>
	 * 1=RuleSpecificCharacterObject,</br>
	 * 2=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame</br>
	 * 3=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions</br>
	 * 4=PrintType,<br/>
	 * 5=List<PageDefinition> - null for static output</br>
	 * Returns: Path to output, if there is any
	 */
	PRINT,

	/**
	 * Return a GUI component to configure a specific roleplaying system plugin
	 * 0=RoleplayingSystem
	 * Returns: A JavaFX Node
	 */
	GET_JAVAFX_CONFIGURATION_COMPONENT,

	/**
	 * Show a GUI to view or modify an existing rulespecific character object
	 * 0=RoleplayingSystem
	 * 1=RuleSpecificCharacterObject,
	 * 2=CharacterHandle
	 * Further optional parameters
	 * 3=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame
	 * 4=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions
	 * Returns: null
	 */
	SHOW_CHARACTER_MODIFICATION_GUI,
	/**
	 * Show a GUI to create a rulespecific character object
	 * 0=RoleplayingSystem
	 * Further optional parameters
	 * 1=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame
	 * 2=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions
	 * Returns: null
	 */
	SHOW_CHARACTER_CREATION_GUI,
	/**
	 * Show a GUI to enter rulespecific data
	 * 0=RoleplayingSystem
	 * Further optional parameters
	 * 1=The Window of the calling parent - e.g. a JavaFX stage or a swing Frame
	 * 2=(Only in Babylon) JavaFX ScreenManager from JavaFXExtensions
	 * Returns: null
	 */
	SHOW_DATA_INPUT_GUI,
	/**
	 * Show a GUI to support a combat
	 * 0 = RoleplayingSystem
	 * 1 = List<Player>
	 * 2 = Characters - RuleSpecificCharacterObject[]
	 * 3 = Info-Level: ALL (all data), VISIBLE (only data visible for others)
	 * 4 = The Window of the calling parent - e.g. a JavaFX stage or a swing Frame
	 * 5 = (Only in Babylon) JavaFX ScreenManager from JavaFXExtensions
	 * Returns: null
	 */
	START_COMBAT,

	/**
	 * Render a JFX region that shows a character and his relevant variable attributes
	 * like health, mana, etc. and eventually some states.
	 * 0 = RoleplayingSystem
	 * 1 = Player
	 * 2 = Character - RuleSpecificCharacterObject
	 * 3 = Size
	 * 4 = Info-Level: ALL (all data), VISIBLE (only data visible for others)
	 * Returns: JFX region
	 */
	RENDER_CHAR_INFO,

	/**
	 * Render a JFX node that shows the necessary info a gamemaster needs to have
	 * about the groups stats (relevant attributes, skills, traits, equipment)
	 * for making secret rolls or decisions.
	 * 0 = RoleplayingSystem
	 * 1 = List<Player>
	 * 2 = Character[] - RuleSpecificCharacterObject
	 * Returns: JFX region
	 */
	RENDER_PARTY_GMINFO,

	/**
	 * Render a JFX node that shows the necessary info a gamemaster needs to have
	 * about the character stats (relevant attributes, skills, traits, equipment)
	 * for making secret rolls or decisions.
	 * 0 = RoleplayingSystem
	 * 1 = Player
	 * 2 = Character - RuleSpecificCharacterObject
	 * Returns: JFX region
	 */
	RENDER_CHARACTER_GMINFO,

	/**
	 * Get a list of information types supported by the plugin
	 * 0 = RoleplayinSystem
	 * Returns: List<WorldInformationType>
	 */
	GET_WORLD_INFO_TYPES,

	/**
	 * Get a list of filters that can be applied to a specific information type
	 * 0 = RoleplayinSystem
	 * 1 = WorldInformationType
	 * Returns: List<Filter>
	 */
	GET_WORLD_INFO_FILTER,

	/**
	 * Get a list of filters that can be applied to a specific information type
	 * 0 = RoleplayinSystem</br>
	 * 1 = WorldInformationType<br/>
	 * 2 = Collection<AppliedFilter> - Filters to apply
	 * Returns: Depends on requested WorldInformationType
	 */
	GET_WORLD_INFO_TABLE,

}
