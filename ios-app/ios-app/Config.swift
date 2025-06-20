import Foundation

enum Config {
    enum Error: Swift.Error {
        case missingKey, invalidValue
    }

    static func value<T>(for key: String) throws -> T where T: LosslessStringConvertible {
        guard let object = Bundle.main.object(forInfoDictionaryKey: key) else {
            throw Error.missingKey
        }

        switch object {
        case let value as T:
            return value
        case let string as String:
            guard let value = T(string) else { fallthrough }
            return value
        default:
            throw Error.invalidValue
        }
    }
}

enum Configuration {
    static var googleClientId: String {
        return try! Config.value(for: "GID_CLIENT_ID")
    }
    
    static var googleRedirectUri: String {
        return try! Config.value(for: "GOOGLE_REDIRECT_URI")
    }
    
    static var apiBaseUrl: String {
        return try! Config.value(for: "API_BASE_URL")
    }
} 