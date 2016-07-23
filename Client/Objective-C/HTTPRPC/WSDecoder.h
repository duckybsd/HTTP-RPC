//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 * Protocol representing a decoder.
 */
@protocol WSDecoder <NSObject>

/**
 * Reads a value from a data object.
 *
 * @param data The data object to read from.
 * @param contentType The MIME type of the data.
 * @param error A description of the problem, in case of an error; <code>nil</code>, otherwise.
 *
 * @return The decoded value.
 */
- (nullable id)readValue:(NSData *)data contentType:(nullable NSString *)contentType error:(NSError * _Nullable *)error;

@end

NS_ASSUME_NONNULL_END